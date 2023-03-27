import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IComptableMatiere, ComptableMatiere } from '../comptable-matiere.model';
import { ComptableMatiereService } from '../service/comptable-matiere.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-comptable-matiere-update',
  templateUrl: './comptable-matiere-update.component.html',
})
export class ComptableMatiereUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nomPrenom: [null, [Validators.required]],
    user: [],
  });

  constructor(
    protected comptableMatiereService: ComptableMatiereService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comptableMatiere }) => {
      this.updateForm(comptableMatiere);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comptableMatiere = this.createFromForm();
    if (comptableMatiere.id !== undefined) {
      this.subscribeToSaveResponse(this.comptableMatiereService.update(comptableMatiere));
    } else {
      this.subscribeToSaveResponse(this.comptableMatiereService.create(comptableMatiere));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComptableMatiere>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(comptableMatiere: IComptableMatiere): void {
    this.editForm.patchValue({
      id: comptableMatiere.id,
      nomPrenom: comptableMatiere.nomPrenom,
      user: comptableMatiere.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, comptableMatiere.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IComptableMatiere {
    return {
      ...new ComptableMatiere(),
      id: this.editForm.get(['id'])!.value,
      nomPrenom: this.editForm.get(['nomPrenom'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}

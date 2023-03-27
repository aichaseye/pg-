import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IChefEtablissement, ChefEtablissement } from '../chef-etablissement.model';
import { ChefEtablissementService } from '../service/chef-etablissement.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-chef-etablissement-update',
  templateUrl: './chef-etablissement-update.component.html',
})
export class ChefEtablissementUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nomPrenom: [null, [Validators.required]],
    user: [],
  });

  constructor(
    protected chefEtablissementService: ChefEtablissementService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chefEtablissement }) => {
      this.updateForm(chefEtablissement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chefEtablissement = this.createFromForm();
    if (chefEtablissement.id !== undefined) {
      this.subscribeToSaveResponse(this.chefEtablissementService.update(chefEtablissement));
    } else {
      this.subscribeToSaveResponse(this.chefEtablissementService.create(chefEtablissement));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChefEtablissement>>): void {
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

  protected updateForm(chefEtablissement: IChefEtablissement): void {
    this.editForm.patchValue({
      id: chefEtablissement.id,
      nomPrenom: chefEtablissement.nomPrenom,
      user: chefEtablissement.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, chefEtablissement.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IChefEtablissement {
    return {
      ...new ChefEtablissement(),
      id: this.editForm.get(['id'])!.value,
      nomPrenom: this.editForm.get(['nomPrenom'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}

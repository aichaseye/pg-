import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IReferant, Referant } from '../referant.model';
import { ReferantService } from '../service/referant.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-referant-update',
  templateUrl: './referant-update.component.html',
})
export class ReferantUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nomPrenom: [null, [Validators.required]],
    user: [],
  });

  constructor(
    protected referantService: ReferantService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ referant }) => {
      this.updateForm(referant);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const referant = this.createFromForm();
    if (referant.id !== undefined) {
      this.subscribeToSaveResponse(this.referantService.update(referant));
    } else {
      this.subscribeToSaveResponse(this.referantService.create(referant));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReferant>>): void {
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

  protected updateForm(referant: IReferant): void {
    this.editForm.patchValue({
      id: referant.id,
      nomPrenom: referant.nomPrenom,
      user: referant.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, referant.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IReferant {
    return {
      ...new Referant(),
      id: this.editForm.get(['id'])!.value,
      nomPrenom: this.editForm.get(['nomPrenom'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}

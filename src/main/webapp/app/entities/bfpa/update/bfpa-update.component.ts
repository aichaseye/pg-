import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBFPA, BFPA } from '../bfpa.model';
import { BFPAService } from '../service/bfpa.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-bfpa-update',
  templateUrl: './bfpa-update.component.html',
})
export class BFPAUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nomPrenom: [null, [Validators.required]],
    user: [],
  });

  constructor(
    protected bFPAService: BFPAService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bFPA }) => {
      this.updateForm(bFPA);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bFPA = this.createFromForm();
    if (bFPA.id !== undefined) {
      this.subscribeToSaveResponse(this.bFPAService.update(bFPA));
    } else {
      this.subscribeToSaveResponse(this.bFPAService.create(bFPA));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBFPA>>): void {
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

  protected updateForm(bFPA: IBFPA): void {
    this.editForm.patchValue({
      id: bFPA.id,
      nomPrenom: bFPA.nomPrenom,
      user: bFPA.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, bFPA.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IBFPA {
    return {
      ...new BFPA(),
      id: this.editForm.get(['id'])!.value,
      nomPrenom: this.editForm.get(['nomPrenom'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}

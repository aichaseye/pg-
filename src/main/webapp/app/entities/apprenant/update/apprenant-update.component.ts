import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApprenant, Apprenant } from '../apprenant.model';
import { ApprenantService } from '../service/apprenant.service';
import { IChefEtablissement } from 'app/entities/chef-etablissement/chef-etablissement.model';
import { ChefEtablissementService } from 'app/entities/chef-etablissement/service/chef-etablissement.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { Sexe } from 'app/entities/enumerations/sexe.model';

@Component({
  selector: 'jhi-apprenant-update',
  templateUrl: './apprenant-update.component.html',
})
export class ApprenantUpdateComponent implements OnInit {
  isSaving = false;
  sexeValues = Object.keys(Sexe);

  chefEtablissementsSharedCollection: IChefEtablissement[] = [];
  etablissementsSharedCollection: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    matriculeApp: [null, []],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    sexe: [null, [Validators.required]],
    telephone: [null, [Validators.required]],
    email: [null, [Validators.required]],
    chefEtablissement: [],
    etablissement: [],
  });

  constructor(
    protected apprenantService: ApprenantService,
    protected chefEtablissementService: ChefEtablissementService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apprenant }) => {
      this.updateForm(apprenant);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apprenant = this.createFromForm();
    if (apprenant.id !== undefined) {
      this.subscribeToSaveResponse(this.apprenantService.update(apprenant));
    } else {
      this.subscribeToSaveResponse(this.apprenantService.create(apprenant));
    }
  }

  trackChefEtablissementById(index: number, item: IChefEtablissement): number {
    return item.id!;
  }

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApprenant>>): void {
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

  protected updateForm(apprenant: IApprenant): void {
    this.editForm.patchValue({
      id: apprenant.id,
      matriculeApp: apprenant.matriculeApp,
      nom: apprenant.nom,
      prenom: apprenant.prenom,
      sexe: apprenant.sexe,
      telephone: apprenant.telephone,
      email: apprenant.email,
      chefEtablissement: apprenant.chefEtablissement,
      etablissement: apprenant.etablissement,
    });

    this.chefEtablissementsSharedCollection = this.chefEtablissementService.addChefEtablissementToCollectionIfMissing(
      this.chefEtablissementsSharedCollection,
      apprenant.chefEtablissement
    );
    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      apprenant.etablissement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.chefEtablissementService
      .query()
      .pipe(map((res: HttpResponse<IChefEtablissement[]>) => res.body ?? []))
      .pipe(
        map((chefEtablissements: IChefEtablissement[]) =>
          this.chefEtablissementService.addChefEtablissementToCollectionIfMissing(
            chefEtablissements,
            this.editForm.get('chefEtablissement')!.value
          )
        )
      )
      .subscribe((chefEtablissements: IChefEtablissement[]) => (this.chefEtablissementsSharedCollection = chefEtablissements));

    this.etablissementService
      .query()
      .pipe(map((res: HttpResponse<IEtablissement[]>) => res.body ?? []))
      .pipe(
        map((etablissements: IEtablissement[]) =>
          this.etablissementService.addEtablissementToCollectionIfMissing(etablissements, this.editForm.get('etablissement')!.value)
        )
      )
      .subscribe((etablissements: IEtablissement[]) => (this.etablissementsSharedCollection = etablissements));
  }

  protected createFromForm(): IApprenant {
    return {
      ...new Apprenant(),
      id: this.editForm.get(['id'])!.value,
      matriculeApp: this.editForm.get(['matriculeApp'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      email: this.editForm.get(['email'])!.value,
      chefEtablissement: this.editForm.get(['chefEtablissement'])!.value,
      etablissement: this.editForm.get(['etablissement'])!.value,
    };
  }
}

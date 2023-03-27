import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEnseignant, Enseignant } from '../enseignant.model';
import { EnseignantService } from '../service/enseignant.service';
import { IBFPA } from 'app/entities/bfpa/bfpa.model';
import { BFPAService } from 'app/entities/bfpa/service/bfpa.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { NomReg } from 'app/entities/enumerations/nom-reg.model';
import { NomDep } from 'app/entities/enumerations/nom-dep.model';
import { CodeIA } from 'app/entities/enumerations/code-ia.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';

@Component({
  selector: 'jhi-enseignant-update',
  templateUrl: './enseignant-update.component.html',
})
export class EnseignantUpdateComponent implements OnInit {
  isSaving = false;
  nomRegValues = Object.keys(NomReg);
  nomDepValues = Object.keys(NomDep);
  codeIAValues = Object.keys(CodeIA);
  sexeValues = Object.keys(Sexe);

  bFPASSharedCollection: IBFPA[] = [];
  etablissementsSharedCollection: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    matriculeEns: [null, []],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    numCNI: [null, [Validators.required]],
    anneeDentree: [null, [Validators.required]],
    region: [null, [Validators.required]],
    departement: [null, [Validators.required]],
    autreDep: [],
    autreRegion: [],
    codeIA: [null, [Validators.required]],
    autreCodeIA: [],
    sexe: [],
    email: [null, [Validators.required]],
    bFPA: [],
    etablissement: [],
  });

  constructor(
    protected enseignantService: EnseignantService,
    protected bFPAService: BFPAService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enseignant }) => {
      this.updateForm(enseignant);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enseignant = this.createFromForm();
    if (enseignant.id !== undefined) {
      this.subscribeToSaveResponse(this.enseignantService.update(enseignant));
    } else {
      this.subscribeToSaveResponse(this.enseignantService.create(enseignant));
    }
  }

  trackBFPAById(index: number, item: IBFPA): number {
    return item.id!;
  }

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnseignant>>): void {
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

  protected updateForm(enseignant: IEnseignant): void {
    this.editForm.patchValue({
      id: enseignant.id,
      matriculeEns: enseignant.matriculeEns,
      nom: enseignant.nom,
      prenom: enseignant.prenom,
      numCNI: enseignant.numCNI,
      anneeDentree: enseignant.anneeDentree,
      region: enseignant.region,
      departement: enseignant.departement,
      autreDep: enseignant.autreDep,
      autreRegion: enseignant.autreRegion,
      codeIA: enseignant.codeIA,
      autreCodeIA: enseignant.autreCodeIA,
      sexe: enseignant.sexe,
      email: enseignant.email,
      bFPA: enseignant.bFPA,
      etablissement: enseignant.etablissement,
    });

    this.bFPASSharedCollection = this.bFPAService.addBFPAToCollectionIfMissing(this.bFPASSharedCollection, enseignant.bFPA);
    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      enseignant.etablissement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bFPAService
      .query()
      .pipe(map((res: HttpResponse<IBFPA[]>) => res.body ?? []))
      .pipe(map((bFPAS: IBFPA[]) => this.bFPAService.addBFPAToCollectionIfMissing(bFPAS, this.editForm.get('bFPA')!.value)))
      .subscribe((bFPAS: IBFPA[]) => (this.bFPASSharedCollection = bFPAS));

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

  protected createFromForm(): IEnseignant {
    return {
      ...new Enseignant(),
      id: this.editForm.get(['id'])!.value,
      matriculeEns: this.editForm.get(['matriculeEns'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      numCNI: this.editForm.get(['numCNI'])!.value,
      anneeDentree: this.editForm.get(['anneeDentree'])!.value,
      region: this.editForm.get(['region'])!.value,
      departement: this.editForm.get(['departement'])!.value,
      autreDep: this.editForm.get(['autreDep'])!.value,
      autreRegion: this.editForm.get(['autreRegion'])!.value,
      codeIA: this.editForm.get(['codeIA'])!.value,
      autreCodeIA: this.editForm.get(['autreCodeIA'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      email: this.editForm.get(['email'])!.value,
      bFPA: this.editForm.get(['bFPA'])!.value,
      etablissement: this.editForm.get(['etablissement'])!.value,
    };
  }
}

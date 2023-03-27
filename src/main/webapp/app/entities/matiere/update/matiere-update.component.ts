import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMatiere, Matiere } from '../matiere.model';
import { MatiereService } from '../service/matiere.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IReferant } from 'app/entities/referant/referant.model';
import { ReferantService } from 'app/entities/referant/service/referant.service';
import { IComptableMatiere } from 'app/entities/comptable-matiere/comptable-matiere.model';
import { ComptableMatiereService } from 'app/entities/comptable-matiere/service/comptable-matiere.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { NomReg } from 'app/entities/enumerations/nom-reg.model';
import { NomDep } from 'app/entities/enumerations/nom-dep.model';
import { CodeIA } from 'app/entities/enumerations/code-ia.model';
import { TypeStructure } from 'app/entities/enumerations/type-structure.model';

@Component({
  selector: 'jhi-matiere-update',
  templateUrl: './matiere-update.component.html',
})
export class MatiereUpdateComponent implements OnInit {
  isSaving = false;
  nomRegValues = Object.keys(NomReg);
  nomDepValues = Object.keys(NomDep);
  codeIAValues = Object.keys(CodeIA);
  typeStructureValues = Object.keys(TypeStructure);

  referantsSharedCollection: IReferant[] = [];
  comptableMatieresSharedCollection: IComptableMatiere[] = [];
  etablissementsSharedCollection: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    matriculeMatiere: [null, []],
    nomMatiere: [],
    reference: [],
    image: [],
    imageContentType: [],
    region: [null, [Validators.required]],
    autreRegion: [],
    departement: [null, [Validators.required]],
    autreDep: [],
    codeIA: [null, [Validators.required]],
    autreCodeIA: [],
    typeStructure: [null, [Validators.required]],
    autreStructure: [],
    anneeAffectation: [null, [Validators.required]],
    referant: [],
    comptableMatiere: [],
    etablissement: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected matiereService: MatiereService,
    protected referantService: ReferantService,
    protected comptableMatiereService: ComptableMatiereService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matiere }) => {
      this.updateForm(matiere);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('jhipsterProjectBaseJwtApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const matiere = this.createFromForm();
    if (matiere.id !== undefined) {
      this.subscribeToSaveResponse(this.matiereService.update(matiere));
    } else {
      this.subscribeToSaveResponse(this.matiereService.create(matiere));
    }
  }

  trackReferantById(index: number, item: IReferant): number {
    return item.id!;
  }

  trackComptableMatiereById(index: number, item: IComptableMatiere): number {
    return item.id!;
  }

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatiere>>): void {
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

  protected updateForm(matiere: IMatiere): void {
    this.editForm.patchValue({
      id: matiere.id,
      matriculeMatiere: matiere.matriculeMatiere,
      nomMatiere: matiere.nomMatiere,
      reference: matiere.reference,
      image: matiere.image,
      imageContentType: matiere.imageContentType,
      region: matiere.region,
      autreRegion: matiere.autreRegion,
      departement: matiere.departement,
      autreDep: matiere.autreDep,
      codeIA: matiere.codeIA,
      autreCodeIA: matiere.autreCodeIA,
      typeStructure: matiere.typeStructure,
      autreStructure: matiere.autreStructure,
      anneeAffectation: matiere.anneeAffectation,
      referant: matiere.referant,
      comptableMatiere: matiere.comptableMatiere,
      etablissement: matiere.etablissement,
    });

    this.referantsSharedCollection = this.referantService.addReferantToCollectionIfMissing(
      this.referantsSharedCollection,
      matiere.referant
    );
    this.comptableMatieresSharedCollection = this.comptableMatiereService.addComptableMatiereToCollectionIfMissing(
      this.comptableMatieresSharedCollection,
      matiere.comptableMatiere
    );
    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      matiere.etablissement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.referantService
      .query()
      .pipe(map((res: HttpResponse<IReferant[]>) => res.body ?? []))
      .pipe(
        map((referants: IReferant[]) =>
          this.referantService.addReferantToCollectionIfMissing(referants, this.editForm.get('referant')!.value)
        )
      )
      .subscribe((referants: IReferant[]) => (this.referantsSharedCollection = referants));

    this.comptableMatiereService
      .query()
      .pipe(map((res: HttpResponse<IComptableMatiere[]>) => res.body ?? []))
      .pipe(
        map((comptableMatieres: IComptableMatiere[]) =>
          this.comptableMatiereService.addComptableMatiereToCollectionIfMissing(
            comptableMatieres,
            this.editForm.get('comptableMatiere')!.value
          )
        )
      )
      .subscribe((comptableMatieres: IComptableMatiere[]) => (this.comptableMatieresSharedCollection = comptableMatieres));

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

  protected createFromForm(): IMatiere {
    return {
      ...new Matiere(),
      id: this.editForm.get(['id'])!.value,
      matriculeMatiere: this.editForm.get(['matriculeMatiere'])!.value,
      nomMatiere: this.editForm.get(['nomMatiere'])!.value,
      reference: this.editForm.get(['reference'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      region: this.editForm.get(['region'])!.value,
      autreRegion: this.editForm.get(['autreRegion'])!.value,
      departement: this.editForm.get(['departement'])!.value,
      autreDep: this.editForm.get(['autreDep'])!.value,
      codeIA: this.editForm.get(['codeIA'])!.value,
      autreCodeIA: this.editForm.get(['autreCodeIA'])!.value,
      typeStructure: this.editForm.get(['typeStructure'])!.value,
      autreStructure: this.editForm.get(['autreStructure'])!.value,
      anneeAffectation: this.editForm.get(['anneeAffectation'])!.value,
      referant: this.editForm.get(['referant'])!.value,
      comptableMatiere: this.editForm.get(['comptableMatiere'])!.value,
      etablissement: this.editForm.get(['etablissement'])!.value,
    };
  }
}

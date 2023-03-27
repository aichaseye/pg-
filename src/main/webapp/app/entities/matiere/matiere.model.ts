import { IReferant } from 'app/entities/referant/referant.model';
import { IComptableMatiere } from 'app/entities/comptable-matiere/comptable-matiere.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { NomReg } from 'app/entities/enumerations/nom-reg.model';
import { NomDep } from 'app/entities/enumerations/nom-dep.model';
import { CodeIA } from 'app/entities/enumerations/code-ia.model';
import { TypeStructure } from 'app/entities/enumerations/type-structure.model';

export interface IMatiere {
  id?: number;
  matriculeMatiere?: string | null;
  nomMatiere?: string | null;
  reference?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  region?: NomReg;
  autreRegion?: string | null;
  departement?: NomDep;
  autreDep?: string | null;
  codeIA?: CodeIA;
  autreCodeIA?: string | null;
  typeStructure?: TypeStructure;
  autreStructure?: string | null;
  anneeAffectation?: number;
  referant?: IReferant | null;
  comptableMatiere?: IComptableMatiere | null;
  etablissement?: IEtablissement | null;
}

export class Matiere implements IMatiere {
  constructor(
    public id?: number,
    public matriculeMatiere?: string | null,
    public nomMatiere?: string | null,
    public reference?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public region?: NomReg,
    public autreRegion?: string | null,
    public departement?: NomDep,
    public autreDep?: string | null,
    public codeIA?: CodeIA,
    public autreCodeIA?: string | null,
    public typeStructure?: TypeStructure,
    public autreStructure?: string | null,
    public anneeAffectation?: number,
    public referant?: IReferant | null,
    public comptableMatiere?: IComptableMatiere | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getMatiereIdentifier(matiere: IMatiere): number | undefined {
  return matiere.id;
}

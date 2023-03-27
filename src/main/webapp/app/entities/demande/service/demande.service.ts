import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemande, getDemandeIdentifier } from '../demande.model';

export type EntityResponseType = HttpResponse<IDemande>;
export type EntityArrayResponseType = HttpResponse<IDemande[]>;

@Injectable({ providedIn: 'root' })
export class DemandeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demandes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(demande: IDemande): Observable<EntityResponseType> {
    return this.http.post<IDemande>(this.resourceUrl, demande, { observe: 'response' });
  }

  update(demande: IDemande): Observable<EntityResponseType> {
    return this.http.put<IDemande>(`${this.resourceUrl}/${getDemandeIdentifier(demande) as number}`, demande, { observe: 'response' });
  }

  partialUpdate(demande: IDemande): Observable<EntityResponseType> {
    return this.http.patch<IDemande>(`${this.resourceUrl}/${getDemandeIdentifier(demande) as number}`, demande, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDemande>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDemande[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemandeToCollectionIfMissing(demandeCollection: IDemande[], ...demandesToCheck: (IDemande | null | undefined)[]): IDemande[] {
    const demandes: IDemande[] = demandesToCheck.filter(isPresent);
    if (demandes.length > 0) {
      const demandeCollectionIdentifiers = demandeCollection.map(demandeItem => getDemandeIdentifier(demandeItem)!);
      const demandesToAdd = demandes.filter(demandeItem => {
        const demandeIdentifier = getDemandeIdentifier(demandeItem);
        if (demandeIdentifier == null || demandeCollectionIdentifiers.includes(demandeIdentifier)) {
          return false;
        }
        demandeCollectionIdentifiers.push(demandeIdentifier);
        return true;
      });
      return [...demandesToAdd, ...demandeCollection];
    }
    return demandeCollection;
  }
}

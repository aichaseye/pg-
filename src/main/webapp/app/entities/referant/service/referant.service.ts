import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReferant, getReferantIdentifier } from '../referant.model';

export type EntityResponseType = HttpResponse<IReferant>;
export type EntityArrayResponseType = HttpResponse<IReferant[]>;

@Injectable({ providedIn: 'root' })
export class ReferantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/referants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(referant: IReferant): Observable<EntityResponseType> {
    return this.http.post<IReferant>(this.resourceUrl, referant, { observe: 'response' });
  }

  update(referant: IReferant): Observable<EntityResponseType> {
    return this.http.put<IReferant>(`${this.resourceUrl}/${getReferantIdentifier(referant) as number}`, referant, { observe: 'response' });
  }

  partialUpdate(referant: IReferant): Observable<EntityResponseType> {
    return this.http.patch<IReferant>(`${this.resourceUrl}/${getReferantIdentifier(referant) as number}`, referant, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReferant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReferant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReferantToCollectionIfMissing(referantCollection: IReferant[], ...referantsToCheck: (IReferant | null | undefined)[]): IReferant[] {
    const referants: IReferant[] = referantsToCheck.filter(isPresent);
    if (referants.length > 0) {
      const referantCollectionIdentifiers = referantCollection.map(referantItem => getReferantIdentifier(referantItem)!);
      const referantsToAdd = referants.filter(referantItem => {
        const referantIdentifier = getReferantIdentifier(referantItem);
        if (referantIdentifier == null || referantCollectionIdentifiers.includes(referantIdentifier)) {
          return false;
        }
        referantCollectionIdentifiers.push(referantIdentifier);
        return true;
      });
      return [...referantsToAdd, ...referantCollection];
    }
    return referantCollection;
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBFPA, getBFPAIdentifier } from '../bfpa.model';

export type EntityResponseType = HttpResponse<IBFPA>;
export type EntityArrayResponseType = HttpResponse<IBFPA[]>;

@Injectable({ providedIn: 'root' })
export class BFPAService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bfpas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bFPA: IBFPA): Observable<EntityResponseType> {
    return this.http.post<IBFPA>(this.resourceUrl, bFPA, { observe: 'response' });
  }

  update(bFPA: IBFPA): Observable<EntityResponseType> {
    return this.http.put<IBFPA>(`${this.resourceUrl}/${getBFPAIdentifier(bFPA) as number}`, bFPA, { observe: 'response' });
  }

  partialUpdate(bFPA: IBFPA): Observable<EntityResponseType> {
    return this.http.patch<IBFPA>(`${this.resourceUrl}/${getBFPAIdentifier(bFPA) as number}`, bFPA, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBFPA>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBFPA[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBFPAToCollectionIfMissing(bFPACollection: IBFPA[], ...bFPASToCheck: (IBFPA | null | undefined)[]): IBFPA[] {
    const bFPAS: IBFPA[] = bFPASToCheck.filter(isPresent);
    if (bFPAS.length > 0) {
      const bFPACollectionIdentifiers = bFPACollection.map(bFPAItem => getBFPAIdentifier(bFPAItem)!);
      const bFPASToAdd = bFPAS.filter(bFPAItem => {
        const bFPAIdentifier = getBFPAIdentifier(bFPAItem);
        if (bFPAIdentifier == null || bFPACollectionIdentifiers.includes(bFPAIdentifier)) {
          return false;
        }
        bFPACollectionIdentifiers.push(bFPAIdentifier);
        return true;
      });
      return [...bFPASToAdd, ...bFPACollection];
    }
    return bFPACollection;
  }
}

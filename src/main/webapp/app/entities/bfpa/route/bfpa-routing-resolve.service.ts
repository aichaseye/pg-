import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBFPA, BFPA } from '../bfpa.model';
import { BFPAService } from '../service/bfpa.service';

@Injectable({ providedIn: 'root' })
export class BFPARoutingResolveService implements Resolve<IBFPA> {
  constructor(protected service: BFPAService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBFPA> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bFPA: HttpResponse<BFPA>) => {
          if (bFPA.body) {
            return of(bFPA.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BFPA());
  }
}

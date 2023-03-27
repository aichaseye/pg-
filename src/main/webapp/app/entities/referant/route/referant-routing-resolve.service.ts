import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReferant, Referant } from '../referant.model';
import { ReferantService } from '../service/referant.service';

@Injectable({ providedIn: 'root' })
export class ReferantRoutingResolveService implements Resolve<IReferant> {
  constructor(protected service: ReferantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReferant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((referant: HttpResponse<Referant>) => {
          if (referant.body) {
            return of(referant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Referant());
  }
}

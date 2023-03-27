import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReferantComponent } from '../list/referant.component';
import { ReferantDetailComponent } from '../detail/referant-detail.component';
import { ReferantUpdateComponent } from '../update/referant-update.component';
import { ReferantRoutingResolveService } from './referant-routing-resolve.service';

const referantRoute: Routes = [
  {
    path: '',
    component: ReferantComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReferantDetailComponent,
    resolve: {
      referant: ReferantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReferantUpdateComponent,
    resolve: {
      referant: ReferantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReferantUpdateComponent,
    resolve: {
      referant: ReferantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(referantRoute)],
  exports: [RouterModule],
})
export class ReferantRoutingModule {}

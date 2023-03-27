import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BFPAComponent } from '../list/bfpa.component';
import { BFPADetailComponent } from '../detail/bfpa-detail.component';
import { BFPAUpdateComponent } from '../update/bfpa-update.component';
import { BFPARoutingResolveService } from './bfpa-routing-resolve.service';

const bFPARoute: Routes = [
  {
    path: '',
    component: BFPAComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BFPADetailComponent,
    resolve: {
      bFPA: BFPARoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BFPAUpdateComponent,
    resolve: {
      bFPA: BFPARoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BFPAUpdateComponent,
    resolve: {
      bFPA: BFPARoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bFPARoute)],
  exports: [RouterModule],
})
export class BFPARoutingModule {}

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReferantComponent } from './list/referant.component';
import { ReferantDetailComponent } from './detail/referant-detail.component';
import { ReferantUpdateComponent } from './update/referant-update.component';
import { ReferantDeleteDialogComponent } from './delete/referant-delete-dialog.component';
import { ReferantRoutingModule } from './route/referant-routing.module';

@NgModule({
  imports: [SharedModule, ReferantRoutingModule],
  declarations: [ReferantComponent, ReferantDetailComponent, ReferantUpdateComponent, ReferantDeleteDialogComponent],
  entryComponents: [ReferantDeleteDialogComponent],
})
export class ReferantModule {}

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BFPAComponent } from './list/bfpa.component';
import { BFPADetailComponent } from './detail/bfpa-detail.component';
import { BFPAUpdateComponent } from './update/bfpa-update.component';
import { BFPADeleteDialogComponent } from './delete/bfpa-delete-dialog.component';
import { BFPARoutingModule } from './route/bfpa-routing.module';

@NgModule({
  imports: [SharedModule, BFPARoutingModule],
  declarations: [BFPAComponent, BFPADetailComponent, BFPAUpdateComponent, BFPADeleteDialogComponent],
  entryComponents: [BFPADeleteDialogComponent],
})
export class BFPAModule {}

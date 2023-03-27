import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBFPA } from '../bfpa.model';
import { BFPAService } from '../service/bfpa.service';

@Component({
  templateUrl: './bfpa-delete-dialog.component.html',
})
export class BFPADeleteDialogComponent {
  bFPA?: IBFPA;

  constructor(protected bFPAService: BFPAService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bFPAService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReferant } from '../referant.model';
import { ReferantService } from '../service/referant.service';

@Component({
  templateUrl: './referant-delete-dialog.component.html',
})
export class ReferantDeleteDialogComponent {
  referant?: IReferant;

  constructor(protected referantService: ReferantService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.referantService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

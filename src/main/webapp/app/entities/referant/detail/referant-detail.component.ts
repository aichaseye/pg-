import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReferant } from '../referant.model';

@Component({
  selector: 'jhi-referant-detail',
  templateUrl: './referant-detail.component.html',
})
export class ReferantDetailComponent implements OnInit {
  referant: IReferant | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ referant }) => {
      this.referant = referant;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

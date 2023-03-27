import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBFPA } from '../bfpa.model';

@Component({
  selector: 'jhi-bfpa-detail',
  templateUrl: './bfpa-detail.component.html',
})
export class BFPADetailComponent implements OnInit {
  bFPA: IBFPA | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bFPA }) => {
      this.bFPA = bFPA;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMatiere } from '../matiere.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-matiere-detail',
  templateUrl: './matiere-detail.component.html',
})
export class MatiereDetailComponent implements OnInit {
  matiere: IMatiere | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matiere }) => {
      this.matiere = matiere;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}

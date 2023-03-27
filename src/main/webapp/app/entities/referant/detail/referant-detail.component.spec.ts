import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReferantDetailComponent } from './referant-detail.component';

describe('Referant Management Detail Component', () => {
  let comp: ReferantDetailComponent;
  let fixture: ComponentFixture<ReferantDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReferantDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ referant: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReferantDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReferantDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load referant on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.referant).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

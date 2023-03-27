import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BFPADetailComponent } from './bfpa-detail.component';

describe('BFPA Management Detail Component', () => {
  let comp: BFPADetailComponent;
  let fixture: ComponentFixture<BFPADetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BFPADetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bFPA: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BFPADetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BFPADetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bFPA on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bFPA).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

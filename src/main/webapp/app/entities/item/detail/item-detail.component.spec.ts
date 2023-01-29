import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemDetailComponent } from './item-detail.component';

describe('Item Management Detail Component', () => {
  let comp: ItemDetailComponent;
  let fixture: ComponentFixture<ItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ item: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load item on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.item).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

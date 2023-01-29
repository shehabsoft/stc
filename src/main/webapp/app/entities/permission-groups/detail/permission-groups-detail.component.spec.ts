import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PermissionGroupsDetailComponent } from './permission-groups-detail.component';

describe('PermissionGroups Management Detail Component', () => {
  let comp: PermissionGroupsDetailComponent;
  let fixture: ComponentFixture<PermissionGroupsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PermissionGroupsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ permissionGroups: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PermissionGroupsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PermissionGroupsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load permissionGroups on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.permissionGroups).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

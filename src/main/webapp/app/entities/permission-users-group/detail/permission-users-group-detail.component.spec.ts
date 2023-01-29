import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PermissionUsersGroupDetailComponent } from './permission-users-group-detail.component';

describe('PermissionUsersGroup Management Detail Component', () => {
  let comp: PermissionUsersGroupDetailComponent;
  let fixture: ComponentFixture<PermissionUsersGroupDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PermissionUsersGroupDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ permissionUsersGroup: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PermissionUsersGroupDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PermissionUsersGroupDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load permissionUsersGroup on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.permissionUsersGroup).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

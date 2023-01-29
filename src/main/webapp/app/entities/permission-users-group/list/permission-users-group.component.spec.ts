import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PermissionUsersGroupService } from '../service/permission-users-group.service';

import { PermissionUsersGroupComponent } from './permission-users-group.component';

describe('PermissionUsersGroup Management Component', () => {
  let comp: PermissionUsersGroupComponent;
  let fixture: ComponentFixture<PermissionUsersGroupComponent>;
  let service: PermissionUsersGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'permission-users-group', component: PermissionUsersGroupComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [PermissionUsersGroupComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(PermissionUsersGroupComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PermissionUsersGroupComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PermissionUsersGroupService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.permissionUsersGroups?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to permissionUsersGroupService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getPermissionUsersGroupIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPermissionUsersGroupIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});

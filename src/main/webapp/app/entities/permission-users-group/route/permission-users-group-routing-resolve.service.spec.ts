import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPermissionUsersGroup } from '../permission-users-group.model';
import { PermissionUsersGroupService } from '../service/permission-users-group.service';

import { PermissionUsersGroupRoutingResolveService } from './permission-users-group-routing-resolve.service';

describe('PermissionUsersGroup routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PermissionUsersGroupRoutingResolveService;
  let service: PermissionUsersGroupService;
  let resultPermissionUsersGroup: IPermissionUsersGroup | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(PermissionUsersGroupRoutingResolveService);
    service = TestBed.inject(PermissionUsersGroupService);
    resultPermissionUsersGroup = undefined;
  });

  describe('resolve', () => {
    it('should return IPermissionUsersGroup returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPermissionUsersGroup = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPermissionUsersGroup).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPermissionUsersGroup = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPermissionUsersGroup).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPermissionUsersGroup>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPermissionUsersGroup = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPermissionUsersGroup).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

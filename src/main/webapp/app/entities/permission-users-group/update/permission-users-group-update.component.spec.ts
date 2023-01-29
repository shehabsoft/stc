import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PermissionUsersGroupFormService } from './permission-users-group-form.service';
import { PermissionUsersGroupService } from '../service/permission-users-group.service';
import { IPermissionUsersGroup } from '../permission-users-group.model';
import { IPermissionGroups } from 'app/entities/permission-groups/permission-groups.model';
import { PermissionGroupsService } from 'app/entities/permission-groups/service/permission-groups.service';

import { PermissionUsersGroupUpdateComponent } from './permission-users-group-update.component';

describe('PermissionUsersGroup Management Update Component', () => {
  let comp: PermissionUsersGroupUpdateComponent;
  let fixture: ComponentFixture<PermissionUsersGroupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let permissionUsersGroupFormService: PermissionUsersGroupFormService;
  let permissionUsersGroupService: PermissionUsersGroupService;
  let permissionGroupsService: PermissionGroupsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PermissionUsersGroupUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PermissionUsersGroupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PermissionUsersGroupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    permissionUsersGroupFormService = TestBed.inject(PermissionUsersGroupFormService);
    permissionUsersGroupService = TestBed.inject(PermissionUsersGroupService);
    permissionGroupsService = TestBed.inject(PermissionGroupsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PermissionGroups query and add missing value', () => {
      const permissionUsersGroup: IPermissionUsersGroup = { id: 456 };
      const permissionGroup: IPermissionGroups = { id: 47189 };
      permissionUsersGroup.permissionGroup = permissionGroup;

      const permissionGroupsCollection: IPermissionGroups[] = [{ id: 98993 }];
      jest.spyOn(permissionGroupsService, 'query').mockReturnValue(of(new HttpResponse({ body: permissionGroupsCollection })));
      const additionalPermissionGroups = [permissionGroup];
      const expectedCollection: IPermissionGroups[] = [...additionalPermissionGroups, ...permissionGroupsCollection];
      jest.spyOn(permissionGroupsService, 'addPermissionGroupsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ permissionUsersGroup });
      comp.ngOnInit();

      expect(permissionGroupsService.query).toHaveBeenCalled();
      expect(permissionGroupsService.addPermissionGroupsToCollectionIfMissing).toHaveBeenCalledWith(
        permissionGroupsCollection,
        ...additionalPermissionGroups.map(expect.objectContaining)
      );
      expect(comp.permissionGroupsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const permissionUsersGroup: IPermissionUsersGroup = { id: 456 };
      const permissionGroup: IPermissionGroups = { id: 75893 };
      permissionUsersGroup.permissionGroup = permissionGroup;

      activatedRoute.data = of({ permissionUsersGroup });
      comp.ngOnInit();

      expect(comp.permissionGroupsSharedCollection).toContain(permissionGroup);
      expect(comp.permissionUsersGroup).toEqual(permissionUsersGroup);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermissionUsersGroup>>();
      const permissionUsersGroup = { id: 123 };
      jest.spyOn(permissionUsersGroupFormService, 'getPermissionUsersGroup').mockReturnValue(permissionUsersGroup);
      jest.spyOn(permissionUsersGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permissionUsersGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permissionUsersGroup }));
      saveSubject.complete();

      // THEN
      expect(permissionUsersGroupFormService.getPermissionUsersGroup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(permissionUsersGroupService.update).toHaveBeenCalledWith(expect.objectContaining(permissionUsersGroup));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermissionUsersGroup>>();
      const permissionUsersGroup = { id: 123 };
      jest.spyOn(permissionUsersGroupFormService, 'getPermissionUsersGroup').mockReturnValue({ id: null });
      jest.spyOn(permissionUsersGroupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permissionUsersGroup: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permissionUsersGroup }));
      saveSubject.complete();

      // THEN
      expect(permissionUsersGroupFormService.getPermissionUsersGroup).toHaveBeenCalled();
      expect(permissionUsersGroupService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermissionUsersGroup>>();
      const permissionUsersGroup = { id: 123 };
      jest.spyOn(permissionUsersGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permissionUsersGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(permissionUsersGroupService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePermissionGroups', () => {
      it('Should forward to permissionGroupsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(permissionGroupsService, 'comparePermissionGroups');
        comp.comparePermissionGroups(entity, entity2);
        expect(permissionGroupsService.comparePermissionGroups).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PermissionGroupsFormService } from './permission-groups-form.service';
import { PermissionGroupsService } from '../service/permission-groups.service';
import { IPermissionGroups } from '../permission-groups.model';

import { PermissionGroupsUpdateComponent } from './permission-groups-update.component';

describe('PermissionGroups Management Update Component', () => {
  let comp: PermissionGroupsUpdateComponent;
  let fixture: ComponentFixture<PermissionGroupsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let permissionGroupsFormService: PermissionGroupsFormService;
  let permissionGroupsService: PermissionGroupsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PermissionGroupsUpdateComponent],
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
      .overrideTemplate(PermissionGroupsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PermissionGroupsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    permissionGroupsFormService = TestBed.inject(PermissionGroupsFormService);
    permissionGroupsService = TestBed.inject(PermissionGroupsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const permissionGroups: IPermissionGroups = { id: 456 };

      activatedRoute.data = of({ permissionGroups });
      comp.ngOnInit();

      expect(comp.permissionGroups).toEqual(permissionGroups);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermissionGroups>>();
      const permissionGroups = { id: 123 };
      jest.spyOn(permissionGroupsFormService, 'getPermissionGroups').mockReturnValue(permissionGroups);
      jest.spyOn(permissionGroupsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permissionGroups });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permissionGroups }));
      saveSubject.complete();

      // THEN
      expect(permissionGroupsFormService.getPermissionGroups).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(permissionGroupsService.update).toHaveBeenCalledWith(expect.objectContaining(permissionGroups));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermissionGroups>>();
      const permissionGroups = { id: 123 };
      jest.spyOn(permissionGroupsFormService, 'getPermissionGroups').mockReturnValue({ id: null });
      jest.spyOn(permissionGroupsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permissionGroups: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permissionGroups }));
      saveSubject.complete();

      // THEN
      expect(permissionGroupsFormService.getPermissionGroups).toHaveBeenCalled();
      expect(permissionGroupsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermissionGroups>>();
      const permissionGroups = { id: 123 };
      jest.spyOn(permissionGroupsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permissionGroups });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(permissionGroupsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

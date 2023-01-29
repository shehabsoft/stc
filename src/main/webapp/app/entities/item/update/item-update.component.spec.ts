import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ItemFormService } from './item-form.service';
import { ItemService } from '../service/item.service';
import { IItem } from '../item.model';
import { IPermissionUsersGroup } from 'app/entities/permission-users-group/permission-users-group.model';
import { PermissionUsersGroupService } from 'app/entities/permission-users-group/service/permission-users-group.service';

import { ItemUpdateComponent } from './item-update.component';

describe('Item Management Update Component', () => {
  let comp: ItemUpdateComponent;
  let fixture: ComponentFixture<ItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemFormService: ItemFormService;
  let itemService: ItemService;
  let permissionUsersGroupService: PermissionUsersGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ItemUpdateComponent],
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
      .overrideTemplate(ItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemFormService = TestBed.inject(ItemFormService);
    itemService = TestBed.inject(ItemService);
    permissionUsersGroupService = TestBed.inject(PermissionUsersGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PermissionUsersGroup query and add missing value', () => {
      const item: IItem = { id: 456 };
      const permissionUsersGroup: IPermissionUsersGroup = { id: 64349 };
      item.permissionUsersGroup = permissionUsersGroup;

      const permissionUsersGroupCollection: IPermissionUsersGroup[] = [{ id: 92032 }];
      jest.spyOn(permissionUsersGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: permissionUsersGroupCollection })));
      const additionalPermissionUsersGroups = [permissionUsersGroup];
      const expectedCollection: IPermissionUsersGroup[] = [...additionalPermissionUsersGroups, ...permissionUsersGroupCollection];
      jest.spyOn(permissionUsersGroupService, 'addPermissionUsersGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(permissionUsersGroupService.query).toHaveBeenCalled();
      expect(permissionUsersGroupService.addPermissionUsersGroupToCollectionIfMissing).toHaveBeenCalledWith(
        permissionUsersGroupCollection,
        ...additionalPermissionUsersGroups.map(expect.objectContaining)
      );
      expect(comp.permissionUsersGroupsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const item: IItem = { id: 456 };
      const permissionUsersGroup: IPermissionUsersGroup = { id: 96820 };
      item.permissionUsersGroup = permissionUsersGroup;

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(comp.permissionUsersGroupsSharedCollection).toContain(permissionUsersGroup);
      expect(comp.item).toEqual(item);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue(item);
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemService.update).toHaveBeenCalledWith(expect.objectContaining(item));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue({ id: null });
      jest.spyOn(itemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(itemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePermissionUsersGroup', () => {
      it('Should forward to permissionUsersGroupService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(permissionUsersGroupService, 'comparePermissionUsersGroup');
        comp.comparePermissionUsersGroup(entity, entity2);
        expect(permissionUsersGroupService.comparePermissionUsersGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

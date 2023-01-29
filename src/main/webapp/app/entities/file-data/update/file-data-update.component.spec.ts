import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FileDataFormService } from './file-data-form.service';
import { FileDataService } from '../service/file-data.service';
import { IFileData } from '../file-data.model';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';

import { FileDataUpdateComponent } from './file-data-update.component';

describe('FileData Management Update Component', () => {
  let comp: FileDataUpdateComponent;
  let fixture: ComponentFixture<FileDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fileDataFormService: FileDataFormService;
  let fileDataService: FileDataService;
  let itemService: ItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FileDataUpdateComponent],
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
      .overrideTemplate(FileDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FileDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fileDataFormService = TestBed.inject(FileDataFormService);
    fileDataService = TestBed.inject(FileDataService);
    itemService = TestBed.inject(ItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Item query and add missing value', () => {
      const fileData: IFileData = { id: 456 };
      const item: IItem = { id: 95196 };
      fileData.item = item;

      const itemCollection: IItem[] = [{ id: 46810 }];
      jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
      const additionalItems = [item];
      const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
      jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fileData });
      comp.ngOnInit();

      expect(itemService.query).toHaveBeenCalled();
      expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(
        itemCollection,
        ...additionalItems.map(expect.objectContaining)
      );
      expect(comp.itemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const fileData: IFileData = { id: 456 };
      const item: IItem = { id: 4307 };
      fileData.item = item;

      activatedRoute.data = of({ fileData });
      comp.ngOnInit();

      expect(comp.itemsSharedCollection).toContain(item);
      expect(comp.fileData).toEqual(fileData);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFileData>>();
      const fileData = { id: 123 };
      jest.spyOn(fileDataFormService, 'getFileData').mockReturnValue(fileData);
      jest.spyOn(fileDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileData }));
      saveSubject.complete();

      // THEN
      expect(fileDataFormService.getFileData).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fileDataService.update).toHaveBeenCalledWith(expect.objectContaining(fileData));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFileData>>();
      const fileData = { id: 123 };
      jest.spyOn(fileDataFormService, 'getFileData').mockReturnValue({ id: null });
      jest.spyOn(fileDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileData: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileData }));
      saveSubject.complete();

      // THEN
      expect(fileDataFormService.getFileData).toHaveBeenCalled();
      expect(fileDataService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFileData>>();
      const fileData = { id: 123 };
      jest.spyOn(fileDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fileDataService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareItem', () => {
      it('Should forward to itemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(itemService, 'compareItem');
        comp.compareItem(entity, entity2);
        expect(itemService.compareItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FileDataService } from '../service/file-data.service';

import { FileDataComponent } from './file-data.component';

describe('FileData Management Component', () => {
  let comp: FileDataComponent;
  let fixture: ComponentFixture<FileDataComponent>;
  let service: FileDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'file-data', component: FileDataComponent }]), HttpClientTestingModule],
      declarations: [FileDataComponent],
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
      .overrideTemplate(FileDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FileDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FileDataService);

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
    expect(comp.fileData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to fileDataService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getFileDataIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getFileDataIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FileDataFormService, FileDataFormGroup } from './file-data-form.service';
import { IFileData } from '../file-data.model';
import { FileDataService } from '../service/file-data.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';

@Component({
  selector: 'jhi-file-data-update',
  templateUrl: './file-data-update.component.html',
})
export class FileDataUpdateComponent implements OnInit {
  isSaving = false;
  fileData: IFileData | null = null;

  itemsSharedCollection: IItem[] = [];

  editForm: FileDataFormGroup = this.fileDataFormService.createFileDataFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected fileDataService: FileDataService,
    protected fileDataFormService: FileDataFormService,
    protected itemService: ItemService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareItem = (o1: IItem | null, o2: IItem | null): boolean => this.itemService.compareItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileData }) => {
      this.fileData = fileData;
      if (fileData) {
        this.updateForm(fileData);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('filemanagerServiceApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fileData = this.fileDataFormService.getFileData(this.editForm);
    if (fileData.id !== null) {
      this.subscribeToSaveResponse(this.fileDataService.update(fileData));
    } else {
      this.subscribeToSaveResponse(this.fileDataService.create(fileData));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileData>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fileData: IFileData): void {
    this.fileData = fileData;
    this.fileDataFormService.resetForm(this.editForm, fileData);

    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing<IItem>(this.itemsSharedCollection, fileData.item);
  }

  protected loadRelationshipsOptions(): void {
    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing<IItem>(items, this.fileData?.item)))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));
  }
}

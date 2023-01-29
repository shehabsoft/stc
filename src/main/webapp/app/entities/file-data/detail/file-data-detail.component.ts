import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFileData } from '../file-data.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-file-data-detail',
  templateUrl: './file-data-detail.component.html',
})
export class FileDataDetailComponent implements OnInit {
  fileData: IFileData | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileData }) => {
      this.fileData = fileData;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}

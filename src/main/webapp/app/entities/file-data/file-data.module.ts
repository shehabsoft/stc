import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FileDataComponent } from './list/file-data.component';
import { FileDataDetailComponent } from './detail/file-data-detail.component';
import { FileDataUpdateComponent } from './update/file-data-update.component';
import { FileDataDeleteDialogComponent } from './delete/file-data-delete-dialog.component';
import { FileDataRoutingModule } from './route/file-data-routing.module';

@NgModule({
  imports: [SharedModule, FileDataRoutingModule],
  declarations: [FileDataComponent, FileDataDetailComponent, FileDataUpdateComponent, FileDataDeleteDialogComponent],
})
export class FileDataModule {}

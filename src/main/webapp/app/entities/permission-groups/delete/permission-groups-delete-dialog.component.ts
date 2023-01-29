import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPermissionGroups } from '../permission-groups.model';
import { PermissionGroupsService } from '../service/permission-groups.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './permission-groups-delete-dialog.component.html',
})
export class PermissionGroupsDeleteDialogComponent {
  permissionGroups?: IPermissionGroups;

  constructor(protected permissionGroupsService: PermissionGroupsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.permissionGroupsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPermissionUsersGroup } from '../permission-users-group.model';
import { PermissionUsersGroupService } from '../service/permission-users-group.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './permission-users-group-delete-dialog.component.html',
})
export class PermissionUsersGroupDeleteDialogComponent {
  permissionUsersGroup?: IPermissionUsersGroup;

  constructor(protected permissionUsersGroupService: PermissionUsersGroupService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.permissionUsersGroupService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

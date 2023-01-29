import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PermissionGroupsFormService, PermissionGroupsFormGroup } from './permission-groups-form.service';
import { IPermissionGroups } from '../permission-groups.model';
import { PermissionGroupsService } from '../service/permission-groups.service';

@Component({
  selector: 'jhi-permission-groups-update',
  templateUrl: './permission-groups-update.component.html',
})
export class PermissionGroupsUpdateComponent implements OnInit {
  isSaving = false;
  permissionGroups: IPermissionGroups | null = null;

  editForm: PermissionGroupsFormGroup = this.permissionGroupsFormService.createPermissionGroupsFormGroup();

  constructor(
    protected permissionGroupsService: PermissionGroupsService,
    protected permissionGroupsFormService: PermissionGroupsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permissionGroups }) => {
      this.permissionGroups = permissionGroups;
      if (permissionGroups) {
        this.updateForm(permissionGroups);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const permissionGroups = this.permissionGroupsFormService.getPermissionGroups(this.editForm);
    if (permissionGroups.id !== null) {
      this.subscribeToSaveResponse(this.permissionGroupsService.update(permissionGroups));
    } else {
      this.subscribeToSaveResponse(this.permissionGroupsService.create(permissionGroups));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPermissionGroups>>): void {
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

  protected updateForm(permissionGroups: IPermissionGroups): void {
    this.permissionGroups = permissionGroups;
    this.permissionGroupsFormService.resetForm(this.editForm, permissionGroups);
  }
}

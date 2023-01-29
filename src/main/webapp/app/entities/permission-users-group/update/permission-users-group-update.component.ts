import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PermissionUsersGroupFormService, PermissionUsersGroupFormGroup } from './permission-users-group-form.service';
import { IPermissionUsersGroup } from '../permission-users-group.model';
import { PermissionUsersGroupService } from '../service/permission-users-group.service';
import { IPermissionGroups } from 'app/entities/permission-groups/permission-groups.model';
import { PermissionGroupsService } from 'app/entities/permission-groups/service/permission-groups.service';
import { PermissionLevel } from 'app/entities/enumerations/permission-level.model';

@Component({
  selector: 'jhi-permission-users-group-update',
  templateUrl: './permission-users-group-update.component.html',
})
export class PermissionUsersGroupUpdateComponent implements OnInit {
  isSaving = false;
  permissionUsersGroup: IPermissionUsersGroup | null = null;
  permissionLevelValues = Object.keys(PermissionLevel);

  permissionGroupsSharedCollection: IPermissionGroups[] = [];

  editForm: PermissionUsersGroupFormGroup = this.permissionUsersGroupFormService.createPermissionUsersGroupFormGroup();

  constructor(
    protected permissionUsersGroupService: PermissionUsersGroupService,
    protected permissionUsersGroupFormService: PermissionUsersGroupFormService,
    protected permissionGroupsService: PermissionGroupsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePermissionGroups = (o1: IPermissionGroups | null, o2: IPermissionGroups | null): boolean =>
    this.permissionGroupsService.comparePermissionGroups(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permissionUsersGroup }) => {
      this.permissionUsersGroup = permissionUsersGroup;
      if (permissionUsersGroup) {
        this.updateForm(permissionUsersGroup);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const permissionUsersGroup = this.permissionUsersGroupFormService.getPermissionUsersGroup(this.editForm);
    if (permissionUsersGroup.id !== null) {
      this.subscribeToSaveResponse(this.permissionUsersGroupService.update(permissionUsersGroup));
    } else {
      this.subscribeToSaveResponse(this.permissionUsersGroupService.create(permissionUsersGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPermissionUsersGroup>>): void {
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

  protected updateForm(permissionUsersGroup: IPermissionUsersGroup): void {
    this.permissionUsersGroup = permissionUsersGroup;
    this.permissionUsersGroupFormService.resetForm(this.editForm, permissionUsersGroup);

    this.permissionGroupsSharedCollection = this.permissionGroupsService.addPermissionGroupsToCollectionIfMissing<IPermissionGroups>(
      this.permissionGroupsSharedCollection,
      permissionUsersGroup.permissionGroup
    );
  }

  protected loadRelationshipsOptions(): void {
    this.permissionGroupsService
      .query()
      .pipe(map((res: HttpResponse<IPermissionGroups[]>) => res.body ?? []))
      .pipe(
        map((permissionGroups: IPermissionGroups[]) =>
          this.permissionGroupsService.addPermissionGroupsToCollectionIfMissing<IPermissionGroups>(
            permissionGroups,
            this.permissionUsersGroup?.permissionGroup
          )
        )
      )
      .subscribe((permissionGroups: IPermissionGroups[]) => (this.permissionGroupsSharedCollection = permissionGroups));
  }
}

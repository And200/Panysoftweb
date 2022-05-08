import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RecipComponent } from './list/recip.component';
import { RecipDetailComponent } from './detail/recip-detail.component';
import { RecipUpdateComponent } from './update/recip-update.component';
import { RecipDeleteDialogComponent } from './delete/recip-delete-dialog.component';
import { RecipRoutingModule } from './route/recip-routing.module';

@NgModule({
  imports: [SharedModule, RecipRoutingModule],
  declarations: [RecipComponent, RecipDetailComponent, RecipUpdateComponent, RecipDeleteDialogComponent],
  entryComponents: [RecipDeleteDialogComponent],
})
export class RecipModule {}

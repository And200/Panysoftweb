import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PresentationComponent } from './list/presentation.component';
import { PresentationDetailComponent } from './detail/presentation-detail.component';
import { PresentationUpdateComponent } from './update/presentation-update.component';
import { PresentationDeleteDialogComponent } from './delete/presentation-delete-dialog.component';
import { PresentationRoutingModule } from './route/presentation-routing.module';

@NgModule({
  imports: [SharedModule, PresentationRoutingModule],
  declarations: [PresentationComponent, PresentationDetailComponent, PresentationUpdateComponent, PresentationDeleteDialogComponent],
  entryComponents: [PresentationDeleteDialogComponent],
})
export class PresentationModule {}

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrderPlacedComponent } from './list/order-placed.component';
import { OrderPlacedDetailComponent } from './detail/order-placed-detail.component';
import { OrderPlacedUpdateComponent } from './update/order-placed-update.component';
import { OrderPlacedDeleteDialogComponent } from './delete/order-placed-delete-dialog.component';
import { OrderPlacedRoutingModule } from './route/order-placed-routing.module';

@NgModule({
  imports: [SharedModule, OrderPlacedRoutingModule],
  declarations: [OrderPlacedComponent, OrderPlacedDetailComponent, OrderPlacedUpdateComponent, OrderPlacedDeleteDialogComponent],
  entryComponents: [OrderPlacedDeleteDialogComponent],
})
export class OrderPlacedModule {}

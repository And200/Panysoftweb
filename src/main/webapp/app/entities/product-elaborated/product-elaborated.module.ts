import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductElaboratedComponent } from './list/product-elaborated.component';
import { ProductElaboratedDetailComponent } from './detail/product-elaborated-detail.component';
import { ProductElaboratedUpdateComponent } from './update/product-elaborated-update.component';
import { ProductElaboratedDeleteDialogComponent } from './delete/product-elaborated-delete-dialog.component';
import { ProductElaboratedRoutingModule } from './route/product-elaborated-routing.module';

@NgModule({
  imports: [SharedModule, ProductElaboratedRoutingModule],
  declarations: [
    ProductElaboratedComponent,
    ProductElaboratedDetailComponent,
    ProductElaboratedUpdateComponent,
    ProductElaboratedDeleteDialogComponent,
  ],
  entryComponents: [ProductElaboratedDeleteDialogComponent],
})
export class ProductElaboratedModule {}

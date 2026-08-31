package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.CategoryCatalog
import com.example.data.model.PersonCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("FitLook AI", appName)
  }

  @Test
  fun `verify category catalog returns clothing options`() {
    val menClothing = CategoryCatalog.getClothingCategoriesFor(PersonCategory.MAN)
    assertTrue(menClothing.contains("Sherwani"))
    assertTrue(menClothing.contains("Shirt"))

    val womenClothing = CategoryCatalog.getClothingCategoriesFor(PersonCategory.WOMAN)
    assertTrue(womenClothing.contains("Saree"))
    assertTrue(womenClothing.contains("Lehenga"))
  }
}

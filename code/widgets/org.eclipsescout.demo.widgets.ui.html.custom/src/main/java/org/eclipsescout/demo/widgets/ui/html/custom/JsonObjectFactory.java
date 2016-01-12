package org.eclipsescout.demo.widgets.ui.html.custom;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.ui.html.IUiSession;
import org.eclipse.scout.rt.ui.html.json.AbstractJsonObjectFactory;
import org.eclipse.scout.rt.ui.html.json.IJsonAdapter;
import org.eclipsescout.demo.widgets.client.custom.ui.form.fields.heatmapfield.IHeatmapField;

@Bean
@Order(100)
public class JsonObjectFactory extends AbstractJsonObjectFactory {

  @Override
  public IJsonAdapter<?> createJsonAdapter(Object model, IUiSession session, String id, IJsonAdapter<?> parent) {
    if (model instanceof IHeatmapField) {
      return new JsonHeatmapField((IHeatmapField) model, session, id, parent);
    }
    return null;
  }

}

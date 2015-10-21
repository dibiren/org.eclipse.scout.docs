/*******************************************************************************
 * Copyright (c) 2015 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipse.scout.contacts.client.template;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.scout.commons.IOUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.contacts.client.Icons;
import org.eclipse.scout.contacts.client.common.PictureUrlForm;
import org.eclipse.scout.contacts.shared.template.AbstractPictureBoxData;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractLinkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.imagebox.AbstractImageField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ExceptionHandler;
import org.eclipse.scout.rt.shared.TEXTS;

@FormData(value = AbstractPictureBoxData.class, sdkCommand = FormData.SdkCommand.CREATE, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE)
public abstract class AbstractPictureBox extends AbstractGroupBox {

  private String m_pictureUrl;

  @Override
  protected boolean getConfiguredBorderVisible() {
    return false;
  }

  @Override
  protected int getConfiguredGridColumnCount() {
    return 1;
  }

  @Override
  protected int getConfiguredGridH() {
    return 5;
  }

  @Override
  protected int getConfiguredGridW() {
    return 1;
  }

  public PictureField getPictureField() {
    return getFieldByClass(PictureField.class);
  }

  protected String getConfiguredImageId() {
    return Icons.File;
  }

  @FormData
  public String getPictureUrl() {
    return m_pictureUrl;
  }

  @FormData
  public void setPictureUrl(String pictureUrl) {
    m_pictureUrl = pictureUrl;

    if (m_pictureUrl == null) {
      getPictureField().setImage(null);
      getForm().touch();
    }
    else {
      try {
        getPictureField().setImage(IOUtility.getContent(new URL((String) m_pictureUrl).openStream()));
        getPictureField().setAutoFit(true);
        getForm().touch();
      }
      catch (MalformedURLException e) {
        BEANS.get(ExceptionHandler.class).handle(new VetoException(TEXTS.get("InvalidImageUrl"), e));
      }
      catch (IOException e) {
        BEANS.get(ExceptionHandler.class).handle(new VetoException(TEXTS.get("FailedToAccessImageFromUrl"), e));
      }
      catch (ProcessingException e) {
        BEANS.get(ExceptionHandler.class).handle(e);
      }
    }
  }

  @Order(1_000.0)
  public class PictureField extends AbstractImageField {

    @Override
    protected boolean getConfiguredAutoFit() {
      return true;
    }

    @Override
    protected int getConfiguredGridH() {
      return 5;
    }

    @Override
    protected String getConfiguredImageId() {
      return AbstractPictureBox.this.getConfiguredImageId();
    }

    @Override
    protected boolean getConfiguredLabelVisible() {
      return false;
    }

    @Order(1_000.0)
    public class EditURLMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("EditURL");
      }

      @Override
      protected void execAction() throws ProcessingException {
        execChangePicture();
      }
    }
  }

  @Order(2_000.0)
  public class EditUrlButton extends AbstractLinkButton {

    @Override
    protected int getConfiguredHorizontalAlignment() {
      return 1;
    }

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("EditURL");
    }

    @Override
    protected boolean getConfiguredProcessButton() {
      return false;
    }

    @Override
    protected void execClickAction() throws ProcessingException {
      execChangePicture();
    }
  }

  protected void execChangePicture() throws ProcessingException {
    String oldUrl = getPictureUrl();

    PictureUrlForm form = new PictureUrlForm();
    if (StringUtility.hasText(oldUrl)) {
      form.getPictureUrlField().setValue(oldUrl);
    }

    form.startModify();
    form.waitFor();
    if (form.isFormStored()) {
      setPictureUrl(form.getPictureUrlField().getValue());
    }
  }
}
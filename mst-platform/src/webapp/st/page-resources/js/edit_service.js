 /*
  * Copyright (c) 2009 eXtensible Catalog Organization
  *
  * This program is free software; you can redistribute it and/or modify it under the terms of the MIT/X11 license. The text of the
  * license can be found at http://www.opensource.org/licenses/mit-license.php and copy of the license can be found on the project
  * website http://www.extensiblecatalog.org/.
  *
  */

YAHOO.namespace("xc.mst.services.alterService");

YAHOO.xc.mst.services.alterService = {


 cancel : function()
    {
        document.editService.action = "listServices.action";
        document.editService.submit();
    }
}

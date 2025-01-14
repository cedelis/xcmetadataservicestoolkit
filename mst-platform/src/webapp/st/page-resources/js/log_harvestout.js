 /*
  * Copyright (c) 2009 eXtensible Catalog Organization
  *
  * This program is free software; you can redistribute it and/or modify it under the terms of the MIT/X11 license. The text of the
  * license can be found at http://www.opensource.org/licenses/mit-license.php and copy of the license can be found on the project
  * website http://www.extensiblecatalog.org/.
  *
  */

YAHOO.namespace("xc.mst.log.harvestOut");

YAHOO.xc.mst.log.harvestOut = {

 downloadFile: function(serviceId)
            {
                window.location = "downloadLogFile.action?logType=HarvestOut&id="+serviceId;
            },

 resetFunction : function(harvestOutLogFileName,serviceId)
            {
                try
                {

                    document.getElementById("harvestOutLogFileName").value = harvestOutLogFileName;
                    document.getElementById("serviceId").value = serviceId;
                    document.harvestOutReset.action = "harvestOutReset.action";
                    document.harvestOutReset.submit();
                }
                catch(e)
                {
                    alert(e);
                }
            },
resetAll: function()
    {
        try
        {
            document.harvestOutReset.action = "harvestOutResetAll.action";
            document.harvestOutReset.submit();
        }
        catch(e)
        {
            alert(e);
        }
    }
}

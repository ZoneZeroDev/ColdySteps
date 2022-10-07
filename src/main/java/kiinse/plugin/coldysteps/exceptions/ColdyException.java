package kiinse.plugin.coldysteps.exceptions;

import kiinse.plugins.darkwaterapi.api.exceptions.DarkWaterBaseException;

@SuppressWarnings("unused")
public class ColdyException extends DarkWaterBaseException {

    public ColdyException() {super();}

    public ColdyException(String message) {super(message);}

    public ColdyException(Throwable cause) {super(cause);}

    public ColdyException(String message, Throwable cause) {super(message, cause);}
}

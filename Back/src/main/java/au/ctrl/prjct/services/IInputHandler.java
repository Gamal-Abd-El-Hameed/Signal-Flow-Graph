package au.ctrl.prjct.services;

import au.ctrl.prjct.payloads.InputPayload;

public interface IInputHandler {
    boolean handleInput(InputPayload payload);
}

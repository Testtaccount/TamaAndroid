package com.tama.q_municate_core.core.command;

import android.os.Bundle;

public interface Command {

    void execute(Bundle bundle) throws Exception;
}
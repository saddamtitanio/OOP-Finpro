package com.finpro.frontend.command.BossCommands;

import com.finpro.frontend.Boss;

public interface BossCommand {
    void execute(Boss boss);
    boolean isExecutable(Boss boss);
    void update(float delta);
}

package net.stuxcrystal.commandhandler;

import net.stuxcrystal.commandhandler.CommandHandler.CommandData;

class CommandExecutionTask implements Runnable {

    private final CommandData data;

    private final CommandExecutor sender;

    private final ArgumentParser arguments;

    CommandExecutionTask(CommandData data, CommandExecutor sender, ArgumentParser arguments) {
        this.data = data;
        this.sender = sender;
        this.arguments = arguments;
    }

    @Override
    public void run() {
        this.data.execute(sender, arguments);
    }

}

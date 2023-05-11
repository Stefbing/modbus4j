/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.serotonin.modbus4j.serial.rtu;

import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.serial.SerialPortWrapper;
import com.serotonin.modbus4j.serial.SerialSlave;
import com.serotonin.modbus4j.sero.log.IOLog;
import com.serotonin.modbus4j.sero.messaging.MessageControl;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * <p>RtuSlave class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class RtuSlave extends SerialSlave {
    // Runtime fields
    private MessageControl conn;
    private String logPath;

    /**
     * <p>Constructor for RtuSlave.</p>
     *
     * @param wrapper a {@link com.serotonin.modbus4j.serial.SerialPortWrapper} object.
     */
    public RtuSlave(SerialPortWrapper wrapper) {
        this(wrapper, null);
    }

    /**
     * <p>Constructor for RtuSlave.</p>
     *
     * @param wrapper a {@link com.serotonin.modbus4j.serial.SerialPortWrapper} object.
     * @param logPath a  file path to log, by zyb.
     */
    public RtuSlave(SerialPortWrapper wrapper, String logPath) {
        super(wrapper);
        this.logPath = logPath;
    }

    /** {@inheritDoc} */
    @Override
    public void start() throws ModbusInitException {
        super.start();

        RtuMessageParser rtuMessageParser = new RtuMessageParser(false);
        RtuRequestHandler rtuRequestHandler = new RtuRequestHandler(this);

        conn = new MessageControl();
        //1判断报文路径：空->不写，非空->下一步
        //2判断路径：无效->创建文件，有效->设置为报文文件路径
        if (StringUtils.isNotBlank(logPath)) {
            File file = new File(logPath);
            if (!file.exists()) {
                if(!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.setIoLog(new IOLog(logPath));
            } else {
                if(file.isDirectory())
                    logPath = logPath + File.separator + "modbus_rtu_s.log";
                conn.setIoLog(new IOLog(logPath));
            }
        }
        conn.setExceptionHandler(getExceptionHandler());

        try {
            conn.start(transport, rtuMessageParser, rtuRequestHandler, null);
            transport.start("Modbus RTU slave");
        }
        catch (IOException e) {
            throw new ModbusInitException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {
        conn.close();
        super.stop();
    }
}

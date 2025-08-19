/*
 * jPOS Project [http://jpos.org]
 * Copyright (C) 2000-2024 jPOS Software SRL
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jpos.iso;

import java.io.IOException;
import java.io.InputStream;

/**
 * ISOMsgFieldPackager is a packager able to pack compound ISOMsgs
 * (one message inside another one, and so on...)
 *
 * @author apr@cs.com.uy
 * @version $Id$
 */
public class ISOMsgFieldPackager extends ISOFieldPackager {
    protected ISOPackager msgPackager;
    protected ISOFieldPackager fieldPackager;

    /**
     * @param fieldPackager low level field packager
     * @param msgPackager ISOMsgField default packager
     */
    public ISOMsgFieldPackager (
            ISOFieldPackager fieldPackager,
            ISOPackager msgPackager)
    {
        super(fieldPackager.getLength(), fieldPackager.getDescription());
        this.msgPackager = msgPackager;
        this.fieldPackager = fieldPackager;
    }
    /**
     * @param c - a component
     * @return packed component
     * @exception ISOException
     */
    @Override
    public byte[] pack (ISOComponent c) throws ISOException {
        if (c instanceof ISOMsg) {
            ISOMsg m = (ISOMsg) c;
            m.recalcBitMap();

            // honor ISOMsg's current position in hierarchy
            int mfn = m.getFieldNumber() >= 0 ? m.getFieldNumber() : 0;
            ISOBinaryField f = new ISOBinaryField(mfn, msgPackager.pack(m));

            if (msgPackager instanceof ISOSubFieldPackager) {
                ISOSubFieldPackager sfp = (ISOSubFieldPackager) msgPackager;

                // If this ISOMsg needs to be packed as part of some non-bitmapped tagged format
                // (not all types covered here), or if the ISOSubFieldPackager has been configured
                // with a specific field number, overriding the one in the ISOMsg.
                if (fieldPackager instanceof TaggedFieldPackagerBase    ||
                    fieldPackager instanceof TaggedFieldPackager        ||
                    fieldPackager instanceof ISOTagStringFieldPackager  ||
                    fieldPackager instanceof ISOTagBinaryFieldPackager  ||
                    sfp.getFieldNumber() > -1)
                {
                    f.setFieldNumber(sfp.getFieldNumber());
                }
            }

            return fieldPackager.pack(f);
        }
        return fieldPackager.pack(c);
    }

    /**
     * @param c - the Component to unpack
     * @param b - binary image
     * @param offset - starting offset within the binary image
     * @return consumed bytes
     * @exception ISOException
     */
    @Override
    public int unpack (ISOComponent c, byte[] b, int offset)
        throws ISOException
    {
        ISOBinaryField f = new ISOBinaryField(0);
        if(msgPackager instanceof ISOSubFieldPackager) {
            ISOSubFieldPackager sfp = (ISOSubFieldPackager) msgPackager;
            f.setFieldNumber(sfp.getFieldNumber());
        }
        int consumed = fieldPackager.unpack(f, b, offset);
        if (f.getValue() != null && c instanceof ISOMsg)
            msgPackager.unpack(c, (byte[]) f.getValue());
        return consumed;
    }

    /**
     * @param c  - the Component to unpack
     * @param in - input stream
     * @throws ISOException
     * @throws IOException
     */
    @Override
    public void unpack (ISOComponent c, InputStream in)
        throws IOException, ISOException
    {
        ISOBinaryField f = new ISOBinaryField(0);
        if(msgPackager instanceof ISOSubFieldPackager) {
            ISOSubFieldPackager sfp = (ISOSubFieldPackager) msgPackager;
            f.setFieldNumber(sfp.getFieldNumber());
        }
        fieldPackager.unpack (f, in);
        if (f.getValue() != null && c instanceof ISOMsg)
            msgPackager.unpack(c, (byte[]) f.getValue());
    }

    @Override
    public ISOComponent createComponent(int fieldNumber) {
        ISOMsg m = new ISOMsg(fieldNumber);
        m.setPackager(msgPackager);
        return m;
    }

    @Override
    public int getMaxPackedLength() {
        return fieldPackager.getLength();
    }
    public ISOPackager getISOMsgPackager() {
        return msgPackager;
    }
    public ISOFieldPackager getISOFieldPackager() {
        return fieldPackager;
    }
}

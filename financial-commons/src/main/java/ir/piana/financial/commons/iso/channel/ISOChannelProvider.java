package ir.piana.financial.commons.iso.channel;

import ir.piana.financial.commons.iso.channel.configs.ChannelInfo;
import ir.piana.financial.commons.iso.channel.impl.AsciiISOChannelSerializer;
import ir.piana.financial.commons.iso.channel.impl.BinaryISOChannelSerializer;
import ir.piana.financial.commons.errors.FinancialWrappedException;
import ir.piana.financial.commons.types.ISOChannelType;

import java.util.*;
import java.util.stream.Collectors;

public class ISOChannelProvider {
    private final LinkedHashMap<String, ISOChannelSerializer> channelSerializerMap;

    private ISOChannelProvider(LinkedHashMap<String, ISOChannelSerializer> channelSerializerMap) {
        this.channelSerializerMap = channelSerializerMap;
    }

    public static ISOChannelProvider create(ChannelInfo... channels) throws FinancialWrappedException {
        return  create(Arrays.asList(channels));
    }

    public static ISOChannelProvider create(List<ChannelInfo> channels) throws FinancialWrappedException {
        LinkedHashMap<String, ISOChannelSerializer> channelSerializerMap = new LinkedHashMap<>();
        for (ChannelInfo channelInfo : channels) {
            channelSerializerMap.put(channelInfo.getName(), switch (ISOChannelType.byName(channelInfo.getIsoChannelType())) {
                case BINARY_CHANNEL -> new BinaryISOChannelSerializer(channelInfo);
                case ASCII_CHANNEL -> new AsciiISOChannelSerializer(channelInfo);
            });
        }
        return new ISOChannelProvider(channelSerializerMap);
    }

    public ISOChannelSerializer getPrimary() {
        return channelSerializerMap.entrySet().iterator().next().getValue();
    }

    public ISOChannelSerializer getIsoChannelSerializer(String name) {
        return channelSerializerMap.getOrDefault(name, null);
    }

    public Class<? extends ISOChannelSerializer> getIsoChannelSerializerClass(String name) {
        ISOChannelSerializer orDefault = channelSerializerMap.getOrDefault(name, null);
        return orDefault != null ? orDefault.getClass() : null;
    }

    public Set<Map.Entry<String, ISOChannelSerializer>> getChannelSerializerEntry() {
        return channelSerializerMap.entrySet();
    }

    public List<Class<? extends ISOChannelSerializer>> getChannelSerializerClasses() {
        return channelSerializerMap.values().stream().map(ISOChannelSerializer::getClass).collect(Collectors.toList());
    }
}